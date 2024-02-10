package entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "equipoacb", schema = "ligaacb", catalog = "")
@NamedQuery(name = "EquipoacbEntity.findAll", query = "SELECT e FROM EquipoacbEntity e")
public class EquipoacbEntity {
    private int idEquipo;
    private String nombreE;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_equipo")
    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    @Basic
    @Column(name = "nombre_e")
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
