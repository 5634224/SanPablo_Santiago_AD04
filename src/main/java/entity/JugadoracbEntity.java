package entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "jugadoracb", schema = "ligaacb", catalog = "")
@NamedQuery(name = "JugadoracbEntity.findAll", query = "SELECT j FROM JugadoracbEntity j")
public class JugadoracbEntity {
    private int idJugador;
    private int nombreJ;
    private String pos;
    private EquipoacbEntity equipoacbByIdEquipo;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_jugador")
    public int getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(int idJugador) {
        this.idJugador = idJugador;
    }

    @Basic
    @Column(name = "nombre_j")
    public int getNombreJ() {
        return nombreJ;
    }

    public void setNombreJ(int nombreJ) {
        this.nombreJ = nombreJ;
    }

    @Basic
    @Column(name = "pos")
    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JugadoracbEntity that = (JugadoracbEntity) o;
        return idJugador == that.idJugador && nombreJ == that.nombreJ && Objects.equals(pos, that.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idJugador, nombreJ, pos);
    }

    @ManyToOne
    @JoinColumn(name = "id_equipo", referencedColumnName = "id_equipo", nullable = false)
    public EquipoacbEntity getEquipoacbByIdEquipo() {
        return equipoacbByIdEquipo;
    }

    public void setEquipoacbByIdEquipo(EquipoacbEntity equipoacbByIdEquipo) {
        this.equipoacbByIdEquipo = equipoacbByIdEquipo;
    }
}
