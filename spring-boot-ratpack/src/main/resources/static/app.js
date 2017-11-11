'use strict';

(function () {

  $(function app() {

    $('#refresh').click(refresh);

    var rat, root;

    if (rat) rat.abort();
    if (root) root.abort();

    function refresh() {

      function fetch(suffix, target) {

        target = $.get('http://localhost:5050' + suffix)
            .then(function replaceData(response) {
              if (response) {
                $('#app').fadeOut(function fetchData() {
                  $('#app').html(response).fadeIn();
                });
              }
            })
            .fail(function handleError(xhr, status, err) {
              console.log('err:', xhr, 'status:', status, 'err:', err)
            });
      }

      fetch('/rat', rat);
      fetch('/', root);
    }
  });

})();
