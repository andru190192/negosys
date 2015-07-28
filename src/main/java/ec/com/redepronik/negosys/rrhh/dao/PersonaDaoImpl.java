package ec.com.redepronik.negosys.rrhh.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class PersonaDaoImpl extends GenericDaoImpl<Persona, Integer> implements
		PersonaDao, Serializable {

	private static final long serialVersionUID = 1L;
}