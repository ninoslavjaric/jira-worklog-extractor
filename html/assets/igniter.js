$(function () {
    $('#table').DataTable({
        columns: [
            {
                data: 'Date',
                render: function (data, type, row) {
                    var date = new Date(data);
                    return moment(new Date(data)).format('YYYY-MM-DD HH:mm:ss');
                    return date.toString();
                }
            },
            {
                data: 'Issue'
            },
            {
                data: 'Spent',
                render: function (data, type, row) {
                    seconds = parseFloat(data).toFixed(2);
                    minutes = Math.floor(seconds / 60); seconds %= 60;
                    hours = Math.floor(minutes / 60); minutes %= 60;
                    return hours + " H; " + minutes + " M; " + seconds + " S;";
                }
            },
            {
                data: 'Comment'
            }
        ],
        order: [[ 0, "desc" ]]
    });
});
