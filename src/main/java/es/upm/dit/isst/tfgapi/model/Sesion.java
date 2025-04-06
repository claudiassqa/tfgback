package es.upm.dit.isst.tfgapi.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Sesion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Future
    private Date fecha;

    private String lugar;

    @Size(min = 3, max = 3)
    private List<@Email @NotEmpty String> tribunal;

    @JsonIgnore
    @OneToMany(mappedBy = "sesion")
    private List<@Valid TFG> tfgs;

    public Sesion() {}

    // Getters y setters

    public Long getId() { return id; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getLugar() { return lugar; }
    public void setLugar(String lugar) { this.lugar = lugar; }

    public List<String> getTribunal() { return tribunal; }
    public void setTribunal(List<String> tribunal) { this.tribunal = tribunal; }

    public List<TFG> getTfgs() { return tfgs; }

    @JsonGetter("tfgs")
    public String[] getEmailsTfgs() {
        if (tfgs != null)
            return tfgs.stream().map(TFG::getAlumno).toArray(String[]::new);
        else
            return new String[0];
    }

    @JsonProperty("tfgs")
    public void setTfgs(List<TFG> tfgs) {
        this.tfgs = tfgs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sesion)) return false;
        return id != null && id.equals(((Sesion) o).id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
