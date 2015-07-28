package ec.com.redepronik.negosys.invfac.dao;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.invfac.entityAux.Cantidad;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class CantidadDaoImpl extends GenericDaoImpl<Cantidad, Integer>
		implements CantidadDao {

}