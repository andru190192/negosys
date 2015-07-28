PrimeFaces.locales['es'] = {
	closeText : 'Cerrar',
	prevText : 'Anterior',
	nextText : 'Siguiente',
	monthNames : [ 'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
			'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre',
			'Diciembre' ],
	monthNamesShort : [ 'Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago',
			'Sep', 'Oct', 'Nov', 'Dic' ],
	dayNames : [ 'Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves',
			'Viernes', 'Sábado' ],
	dayNamesShort : [ 'Dom', 'Lun', 'Mar', 'Mie', 'Jue', 'Vie', 'Sab' ],
	dayNamesMin : [ 'D', 'L', 'M', 'X', 'J', 'V', 'S' ],
	weekHeader : 'Semana',
	firstDay : 1,
	isRTL : false,
	showMonthAfterYear : false,
	yearSuffix : '',
	timeOnlyTitle : 'Sólo hora',
	timeText : 'Tiempo',
	hourText : 'Hora',
	minuteText : 'Minuto',
	secondText : 'Segundo',
	currentText : 'Fecha actual',
	ampm : false,
	month : 'Mes',
	week : 'Semana',
	day : 'Día',
	allDayText : 'Todo el día'
};

function estiloFila(id, split, idTabla,sumador) {
	var fila = id.split(":");
	var numero = parseInt(fila[split]);
	document.getElementById(idTabla).getElementsByTagName('tr')[numero + sumador]
			.setAttribute("class", "ui-state-hover");

}
function sinEstiloFila(id, split, idTabla,sumador) {
	var fila = id.split(":");
	var numero = parseInt(fila[split]);
	document.getElementById(idTabla).getElementsByTagName('tr')[numero + sumador]
			.setAttribute("class", "sinEstiloFila");
}

function teclaAbajoArriba(keyCode, idComponente, idTabla) {
	var tabla = idTabla;
	var vectorIdComponente = idComponente.split(":");
	var idComponenteMitad = vectorIdComponente[0] + ":" + vectorIdComponente[1];
	var idComponenteUltimo = vectorIdComponente[3];
	console.log("tabla - " + tabla);
	console.log("vectorIdComponente - " + vectorIdComponente);
	console.log("idComponenteMitad - " + idComponenteMitad);
	console.log("idComponenteUltimo - " + idComponenteUltimo);

	var posSplit = idComponente.split(":");
	var p = parseInt(posSplit[2]);
	var n = document.getElementById(tabla).childNodes.length;
	if (keyCode == 38) {
		if (p == 0)
			p = n - 1;
		else
			p = p - 1;
		document.getElementById(
				idComponenteMitad + ':' + p + ':' + idComponenteUltimo).focus();
		return false;
	} else if (keyCode == 40) {
		if ((p + 1) == n)
			p = 0;
		else
			p = p + 1;
		document.getElementById(
				idComponenteMitad + ':' + p + ':' + idComponenteUltimo).focus();
		return false;
	}
}