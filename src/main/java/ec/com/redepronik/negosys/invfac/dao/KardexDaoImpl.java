package ec.com.redepronik.negosys.invfac.dao;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.invfac.entity.Kardex;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class KardexDaoImpl extends GenericDaoImpl<Kardex, Long> implements
		KardexDao {

}