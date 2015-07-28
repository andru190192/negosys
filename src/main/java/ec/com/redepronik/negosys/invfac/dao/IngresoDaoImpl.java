package ec.com.redepronik.negosys.invfac.dao;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.invfac.entity.Ingreso;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class IngresoDaoImpl extends GenericDaoImpl<Ingreso, Long> implements
		IngresoDao {

}
