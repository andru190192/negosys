package ec.com.redepronik.negosys.invfac.dao;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.invfac.entity.Credito;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class CreditoDaoImpl extends GenericDaoImpl<Credito, Integer> implements
		CreditoDao {

}