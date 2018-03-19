var chartConfig = {
  "type": "serial",
  "dataProvider": [],
  "valueAxes": [{
    "gridColor": "#FFFFFF",
    "gridAlpha": 0.2,
    "dashLength": 0
  }],
  "gridAboveGraphs": true,
  "startDuration": 1,
  "graphs": [],
  "chartCursor": {
    "categoryBalloonEnabled": false,
    "cursorAlpha": 0,
    "zoomable": false
  },
  "categoryField": "category",
  "categoryAxis": {
    "gridPosition": "start",
    "gridAlpha": 0
  }
};

jQuery(document).ready(function() {
  // get categories
  jQuery('#data thead th').each(function(index) {
    if (index) { // skip the first column
      chartConfig.graphs.push({
        "balloonText": "[[category]]: <b>[[value]]</b>",
        "fillAlphas": 0.8,
        "lineAlpha": 0.2,
        "type": "column",
        "valueField": "value" + index
      });
    }
  });

  // get data points
  jQuery('#data tbody tr').each(function(index) {
    var dataPoint = {};
    jQuery(this).find('th,td').each(function(index2) {
      if (jQuery(this).is('th')) { // category
        dataPoint.category = this.innerHTML;
      } else {
        dataPoint['value' + index2] = Number(this.innerHTML);
      }
    });
    chartConfig.dataProvider.push(dataPoint);
  });

  // make the chart
  var chart = AmCharts.makeChart("chartdiv", chartConfig);
});
