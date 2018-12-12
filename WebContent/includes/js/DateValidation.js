$(function() {
    
    function cb(start, end) {
        $('#reportrange span').html(start.format('MM/DD/yyyy  HH:MM') + ' - ' + end.format('MM/DD/yyyy HH:MM'));
    }
    cb(moment().subtract(29, 'days'), moment());

    $('#reportrange').daterangepicker({
        ranges: {
            'Today': [moment(), moment()],
           
            'Last 7 Days': [moment().subtract(6, 'days'), moment()],
            'Last 30 Days': [moment().subtract(29, 'days'), moment()],
            'This Month': [moment().startOf('month'), moment().endOf('month')],
            'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
            'Transactions Until': [moment()]
        },
        timePicker: true,
        timePicker24Hour: true,
        timePickerIncrement: 1,
        locale: {
            format: 'MM/DD/yyyy HH:mm'
        }
    }, cb);

});
                         