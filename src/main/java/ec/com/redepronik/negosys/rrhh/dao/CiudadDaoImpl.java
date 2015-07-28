package ec.com.redepronik.negosys.rrhh.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.rrhh.entity.Ciudad;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class CiudadDaoImpl extends GenericDaoImpl<Ciudad, Integer> implements
		CiudadDao, Serializable {
	private static final long serialVersionUID = 1L;

}