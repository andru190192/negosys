package ec.com.redepronik.negosys.invfac.dao;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.invfac.entity.Tarifa;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class TarifaDaoImpl extends GenericDaoImpl<Tarifa, Short> implements
		TarifaDao {

}