package ec.com.redepronik.negosys.seguridad.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.seguridad.entity.Rol;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class RolDaoImpl extends GenericDaoImpl<Rol, Integer> implements RolDao,
		Serializable {

	private static final long serialVersionUID = 1L;

}
