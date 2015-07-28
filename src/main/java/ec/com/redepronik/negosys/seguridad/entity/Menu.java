package ec.com.redepronik.negosys.seguridad.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "menu")
public class Menu implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String icono;
	private Integer nivel;
	private String nombre;
	private Boolean visible;
	private Integer padre;
	private String vista;
	private List<RolMenu> rolMenus;

	public Menu() {
	}

	public Menu(Integer id, String icono, Integer nivel, String nombre,
			Boolean visible, Integer padre, String vista, List<RolMenu> rolMenus) {
		this.id = id;
		this.icono = icono;
		this.nivel = nivel;
		this.nombre = nombre;
		this.visible = visible;
		this.padre = padre;
		this.vista = vista;
		this.rolMenus = rolMenus;
	}

	public RolMenu addRolMenus(RolMenu rolMenus) {
		getRolMenus().add(rolMenus);
		rolMenus.setMenu(this);

		return rolMenus;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Menu other = (Menu) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Column(length = 20)
	public String getIcono() {
		return this.icono;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "MENU_MENUID_GENERATOR", sequenceName = "MENU_MENUID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MENU_MENUID_GENERATOR")
	@Column(name = "menuid", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public Integer getNivel() {
		return this.nivel;
	}

	@Column(nullable = false, length = 25)
	public String getNombre() {
		return this.nombre;
	}

	public Integer getPadre() {
		return this.padre;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "menu")
	public List<RolMenu> getRolMenus() {
		return this.rolMenus;
	}

	@Column(nullable = false)
	public Boolean getVisible() {
		return visible;
	}

	@Column(length = 100)
	public String getVista() {
		return this.vista;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public RolMenu removeRolMenus(RolMenu rolMenus) {
		getRolMenus().remove(rolMenus);
		rolMenus.setMenu(null);

		return rolMenus;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setPadre(Integer padre) {
		this.padre = padre;
	}

	public void setRolMenus(List<RolMenu> rolMenus) {
		this.rolMenus = rolMenus;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public void setVista(String vista) {
		this.vista = vista;
	}

}