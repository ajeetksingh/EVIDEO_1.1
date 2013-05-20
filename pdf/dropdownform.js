$(document).ready(function(){
        $('#pdfSync-trigger').click(function(){
                $(this).next('#pdfSync-content').slideToggle();
                $(this).toggleClass('active');                                  

                if ($(this).hasClass('active')) $(this).find('span').html('&#x25B2;')
                        else $(this).find('span').html('&#x25BC;')
                })
});
