$('document').ready(function(){
    $('table #editButton').on('click', function(event){
        event.preventDefault();
        var href= $(this).attr('href');
        $.get(href, function (category, status){
            $('#idEdit').val(category.id);
            $('#nameEdit').val(category.name);
        });
        $('#editModal').modal();
    })
})

/*
*
* Calling jQuery() (or $()) with an id selector as its argument will return a jQuery object containing a collection of either zero or one DOM element.
* val():用來讀取或者設定修改匹配的value值
*  */