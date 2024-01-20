package entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "jugadoracb", schema = "ligaacb", catalog = "")
public class JugadoracbEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_jugador")
    private int idJugador;
    @Basic
    @Column(name = "nombre_j")
    private int nombreJ;
    @Basic
    @Column(name = "pos")
    private String pos;
    @Basic
    @Column(name = "id_equipo")
    private int idEquipo;

    public int getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(int idJugador) {
        this.idJugador = idJugador;
    }

    public int getNombreJ() {
        return nombreJ;
    }

    public void setNombreJ(int nombreJ) {
        this.nombreJ = nombreJ;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JugadoracbEntity that = (JugadoracbEntity) o;
        return idJugador == that.idJugador && nombreJ == that.nombreJ && idEquipo == that.idEquipo && Objects.equals(pos, that.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idJugador, nombreJ, pos, idEquipo);
    }
}
