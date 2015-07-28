package ec.com.redepronik.negosys.invfac.dao;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.invfac.entityAux.StockProductoReporte;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class StockProductoReporteDaoImpl extends
		GenericDaoImpl<StockProductoReporte, Long> implements
		StockProductoReporteDao {

}