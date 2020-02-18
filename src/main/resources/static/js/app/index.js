var main = {
    init : function () {
        var _this = this;
        $('#btn-save').on('click', function () {
            _this.save();
        });

        $('#btn-update').on('click', function () {
            _this.update();
        });

        $('#btn-delete').on('click', function () {
            _this.delete();
        });
    },
    save : function () {
        var file = $('#img')[0].files[0];
        var formData = new FormData();
        formData.append('file', file);

        var json = '{ "title" : "test",' +
            '         "content" : "test",' +
            '          "user" : {"id": 1L},' +
            '          "startDate" : "2020-01-01",' +
            '          "endDate" : "2020-03-28"' +
            '}';
        formData.append("requestJson", JSON.stringify(json));
        $.ajax({
            type: 'POST',
            url: '/api/mission',
            data: formData,
            processData: false,
            contentType: false
        }).done(function (data) {
            $('#result-image').attr("src", data);
        }).fail(function (error) {
            alert(error);
        })
    },
    update : function () {
        var data = {
            title: $('#title').val(),
            content: $('#content').val()
        };

        var id = $('#id').val();

        $.ajax({
            type: 'PUT',
            url: '/api/post/'+id,
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('글이 수정되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    delete : function () {
        var id = $('#id').val();

        $.ajax({
            type: 'DELETE',
            url: '/api/post/'+id,
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function() {
            alert('글이 삭제되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }

};

main.init();