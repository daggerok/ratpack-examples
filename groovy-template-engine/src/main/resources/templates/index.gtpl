yieldUnescaped '<!DOCTYPE html>'
html(lang:'en') {
  head {
    link(
      'rel': 'stylesheet',
      'href': 'http://127.0.0.1:8080/webjars/bootstrap/3.3.6/css/bootstrap.min.css'
    )
    meta('http-equiv': '"Content-Type" content="text/html; charset=utf-8"')
    title('My page')
  }
  body {
    p("${message}")
    users.each {
      p("${it}")
    }
  }
}
