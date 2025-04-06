package es.upm.dit.isst.tfgapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@Entity
public class TFG {
    @Id
    @Email
    private String alumno;

    @Email
    @NotEmpty
    private String tutor;

    @NotEmpty
    private String titulo;

    private String resumen;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @JsonIgnore
    @Lob
    private byte[] memoria;

    @PositiveOrZero
    @DecimalMax("10.0")
    private Double calificacion;

    private Boolean matriculaHonor;

    @ManyToOne
    private Sesion sesion;

    public TFG() {}

    // Getters y setters

    public String getAlumno() { return alumno; }
    public void setAlumno(String alumno) { this.alumno = alumno; }

    public String getTutor() { return tutor; }
    public void setTutor(String tutor) { this.tutor = tutor; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getResumen() { return resumen; }
    public void setResumen(String resumen) { this.resumen = resumen; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public byte[] getMemoria() { return memoria; }

    @JsonProperty
    public void setMemoria(byte[] memoria) {
        this.memoria = memoria;
    }

    @JsonGetter("memoria")
    public URI getDireccionMemoria() throws URISyntaxException {
        return new URI("./memoria");
    }

    public Double getCalificacion() { return calificacion; }
    public void setCalificacion(Double calificacion) { this.calificacion = calificacion; }

    public Boolean getMatriculaHonor() { return matriculaHonor; }
    public void setMatriculaHonor(Boolean matriculaHonor) { this.matriculaHonor = matriculaHonor; }

    public Sesion getSesion() { return sesion; }
    public void setSesion(Sesion sesion) { this.sesion = sesion; }

    // equals y hashCode solo con el ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TFG)) return false;
        return alumno.equals(((TFG) o).alumno);
    }

    @Override
    public int hashCode() {
        return alumno.hashCode();
    }
}
