$(document).ready(function () {
    if (sessionStorage.getItem("current") !== null) {
        const currentTabName = sessionStorage.getItem("current");
        const currentTab = document.querySelector("[data-tab=" + currentTabName + "]");

        $('ul.tabs li').removeClass('current');
        $('.tab-content').removeClass('current');

        currentTab.classList.add('current');
        $("#" + currentTabName).addClass('current');
    }

    $('ul.tabs li').click(function () {
        var tab_id = $(this).attr('data-tab');

        $('ul.tabs li').removeClass('current');
        $('.tab-content').removeClass('current');

        $(this).addClass('current');
        $("#" + tab_id).addClass('current');

        sessionStorage.setItem("current", tab_id);
    });
    
});
