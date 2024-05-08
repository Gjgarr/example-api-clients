package apiclient.httpexchange;

import apiclient.dto.RequestDto;
import apiclient.dto.ResponseDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface CoolApiInterface {

    @GetExchange("/{resourceId}")
    ResponseDto fetch(@PathVariable Integer resourceId);

    @PostExchange("/")
    ResponseDto create(@RequestBody RequestDto requestDto);
}
