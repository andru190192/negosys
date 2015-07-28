package ec.com.redepronik.negosys.rrhh.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.rrhh.entity.Empleado;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class EmpleadoDaoImpl extends GenericDaoImpl<Empleado, Integer>
		implements EmpleadoDao, Serializable {

	private static final long serialVersionUID = 1L;
}