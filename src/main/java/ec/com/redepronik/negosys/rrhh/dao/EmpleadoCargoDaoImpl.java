package ec.com.redepronik.negosys.rrhh.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.rrhh.entity.EmpleadoCargo;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class EmpleadoCargoDaoImpl extends
		GenericDaoImpl<EmpleadoCargo, Integer> implements EmpleadoCargoDao,
		Serializable {

	private static final long serialVersionUID = 1L;

}