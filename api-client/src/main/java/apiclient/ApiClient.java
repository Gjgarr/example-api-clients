package apiclient;

import apiclient.dto.RequestDto;
import apiclient.dto.ResponseDto;
import apiclient.exceptions.ErrorResponseException;
import apiclient.exceptions.ResourceConflictException;
import apiclient.exceptions.ResourceNotFoundException;

public interface ApiClient {
    /**
     * Get a resource by id
     *
     * @param resourceId resource id
     * @return response
     * @throws ResourceNotFoundException upon resource not found
     * @throws ErrorResponseException    upon any bad request
     */
    ResponseDto fetch(Integer resourceId);

    /**
     * Create a new resource
     *
     * @param requestDto resource
     * @return created resource
     * @throws ResourceConflictException upon resource conflicting
     * @throws ErrorResponseException    upon any bad request
     */
    ResponseDto create(RequestDto requestDto);
}
