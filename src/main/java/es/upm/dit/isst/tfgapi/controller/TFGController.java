package es.upm.dit.isst.tfgapi.controller;

import es.upm.dit.isst.tfgapi.model.*;
import es.upm.dit.isst.tfgapi.repository.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@RestController
@RequestMapping("/myApi")
public class TFGController {

    private final TFGRepository tfgRepository;
    private final SesionRepository sesionRepository;
    public static final Logger log = LoggerFactory.getLogger(TFGController.class);

    public TFGController(TFGRepository t, SesionRepository s) {
        this.tfgRepository = t;
        this.sesionRepository = s;
    }

    @GetMapping("/tfgs")
    public List<TFG> readAll(@RequestParam(name = "tutor", required = false) String tutor) {
        return tutor != null && !tutor.isEmpty() ? tfgRepository.findByTutor(tutor) : (List<TFG>) tfgRepository.findAll();
    }

    @PostMapping("/tfgs")
    public ResponseEntity<TFG> create(@RequestBody TFG newTFG) throws URISyntaxException {
        if (tfgRepository.findById(newTFG.getAlumno()).isPresent())
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        // TODO: validaciones de matrícula y notificación
        TFG result = tfgRepository.save(newTFG);
        return ResponseEntity.created(new URI("/tfgs/" + result.getAlumno())).body(result);
    }

    @GetMapping("/tfgs/{id}")
    public ResponseEntity<TFG> readOne(@PathVariable String id) {
        return tfgRepository.findById(id)
                .map(tfg -> ResponseEntity.ok().body(tfg))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/tfgs/{id}")
    public ResponseEntity<TFG> update(@RequestBody TFG newTFG, @PathVariable String id) {
        return tfgRepository.findById(id).map(tfg -> {
            tfg.setTutor(newTFG.getTutor());
            tfg.setTitulo(newTFG.getTitulo());
            tfg.setResumen(newTFG.getResumen());
            tfg.setEstado(newTFG.getEstado());
            tfg.setMemoria(newTFG.getMemoria());
            tfg.setCalificacion(newTFG.getCalificacion());
            tfg.setMatriculaHonor(newTFG.getMatriculaHonor());
            tfg.setSesion(newTFG.getSesion());
            return ResponseEntity.ok(tfgRepository.save(tfg));
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/tfgs/{id}")
    public ResponseEntity<TFG> partialUpdate(@RequestBody TFG newTFG, @PathVariable String id) {
        return tfgRepository.findById(id).map(tfg -> {
            if (newTFG.getTutor() != null) tfg.setTutor(newTFG.getTutor());
            if (newTFG.getTitulo() != null) tfg.setTitulo(newTFG.getTitulo());
            if (newTFG.getResumen() != null) tfg.setResumen(newTFG.getResumen());
            if (newTFG.getEstado() != null) tfg.setEstado(newTFG.getEstado());
            if (newTFG.getMemoria() != null) tfg.setMemoria(newTFG.getMemoria());
            if (newTFG.getCalificacion() != null) tfg.setCalificacion(newTFG.getCalificacion());
            if (newTFG.getMatriculaHonor() != null) tfg.setMatriculaHonor(newTFG.getMatriculaHonor());
            if (newTFG.getSesion() != null) tfg.setSesion(newTFG.getSesion());
            return ResponseEntity.ok(tfgRepository.save(tfg));
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/tfgs/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        tfgRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/tfgs/{id}/estado/{estado}")
    @Transactional
    public ResponseEntity<?> actualizaEstado(@PathVariable String id, @PathVariable Estado estado) {
        return tfgRepository.findById(id).map(tfg -> {
            if (!tfg.getEstado().canTransitionTo(estado))
                return ResponseEntity.badRequest().body("Transición no válida de " + tfg.getEstado() + " a " + estado);
            tfg.setEstado(estado);
            return ResponseEntity.ok(tfgRepository.save(tfg));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/tfgs/{id}/memoria", consumes = "application/pdf")
    public ResponseEntity<?> subirMemoria(@PathVariable String id, @RequestBody byte[] fileContent) {
        return tfgRepository.findById(id).map(tfg -> {
            tfg.setMemoria(fileContent);
            tfgRepository.save(tfg);
            return ResponseEntity.ok("Memoria subida correctamente");
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TFG no encontrado"));
    }

    @GetMapping(value = "/tfgs/{id}/memoria", produces = "application/pdf")
    public ResponseEntity<?> descargarMemoria(@PathVariable String id) {
        TFG tfg = tfgRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "TFG no encontrado"));
        if (tfg.getMemoria() == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"tfg_" + id + ".pdf\"")
                .body(new ByteArrayResource(tfg.getMemoria()));
    }

    @PostMapping("/sesiones")
    public ResponseEntity<Sesion> crearSesion(@RequestBody Sesion newSesion) throws URISyntaxException {
        Sesion result = sesionRepository.save(newSesion);
        return ResponseEntity.created(new URI("/sesiones/" + result.getId())).body(result);
    }

    @PostMapping("/sesiones/{id}/tfgs")
    public ResponseEntity<?> asignarTFG(@PathVariable Long id, @RequestBody String alumno) {
        return sesionRepository.findById(id).map(sesion -> {
            TFG tfg = tfgRepository.findById(alumno).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "TFG no encontrado"));
            tfg.setSesion(sesion);
            tfgRepository.save(tfg);
            return ResponseEntity.ok(tfg);
        }).orElse(ResponseEntity.notFound().build());
    }
}

