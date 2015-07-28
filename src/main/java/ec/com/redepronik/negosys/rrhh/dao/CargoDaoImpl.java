package ec.com.redepronik.negosys.rrhh.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.rrhh.entity.Cargo;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class CargoDaoImpl extends GenericDaoImpl<Cargo, Integer> implements
		CargoDao, Serializable {
	private static final long serialVersionUID = 1L;

}