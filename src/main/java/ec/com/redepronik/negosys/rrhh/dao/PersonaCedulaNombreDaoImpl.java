package ec.com.redepronik.negosys.rrhh.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.invfac.entityAux.PersonaCedulaNombre;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class PersonaCedulaNombreDaoImpl extends
		GenericDaoImpl<PersonaCedulaNombre, Integer> implements
		PersonaCedulaNombreDao, Serializable {
	private static final long serialVersionUID = 1L;

}