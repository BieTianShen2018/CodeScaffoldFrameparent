package com.dayday.up.costom.exception;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 2020-07-29
 * Time: 11:38
 */
public class MyException {

//    @ExceptionHandler(NoHandlerFoundException.class)
//    @ResponseStatus(value = HttpStatus.NOT_FOUND)
//    @ResponseBody
//    public ResponseEntity<ErrorResponse> handle404(NoHandlerFoundException e) {
//
//        LOGGER.info("Resource Not found, RequestURL: {}, HttpMethod: {}, Headers: {}", e.getRequestURL(),
//                e.getHttpMethod(), e.getHeaders());
//        LOGGER.error(e.getMessage(), e);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//
//        return new ResponseEntity<>(new ErrorResponse(WebExceptionCode.NOT_FOUND), headers, HttpStatus.NOT_FOUND);
//    }
}
