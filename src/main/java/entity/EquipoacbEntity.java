package entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "equipoacb", schema = "ligaacb", catalog = "")
public class EquipoacbEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_equipo")
    private int idEquipo;
    @Basic
    @Column(name = "nombre_e")
    private String nombreE;

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombreE() {
        return nombreE;
    }

    public void setNombreE(String nombreE) {
        this.nombreE = nombreE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EquipoacbEntity that = (EquipoacbEntity) o;
        return idEquipo == that.idEquipo && Objects.equals(nombreE, that.nombreE);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEquipo, nombreE);
    }
}
