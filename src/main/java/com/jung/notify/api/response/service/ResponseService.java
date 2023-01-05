package com.jung.notify.api.response.service;

import com.jung.notify.api.response.error.ErrorCode;
import com.jung.notify.api.response.model.CommonResult;
import com.jung.notify.api.response.model.ListResult;
import com.jung.notify.api.response.model.PagingListResult;
import com.jung.notify.api.response.model.SingleResult;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    public enum CommonResponse {
        SUCCESS(200,"200", "SUCCESS");

        int status;
        String code;
        String msg;

        CommonResponse(int status, String code, String msg) {
            this.status = status;
            this.code = code;
            this.msg = msg;
        }

        public int getStatus() {
            return status;
        }

        public String getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    private void setSuccessResult(CommonResult result) {
        result.setStatus(CommonResponse.SUCCESS.getStatus());
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMessage(CommonResponse.SUCCESS.getMsg());
    }

    // 단일 건 결과 처리
    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    // 다중 건 결과 처리
    public <T> ListResult<T> getListResult(List<T> list) {
        ListResult<T> result = new ListResult<>();
        result.setData(list);
        setSuccessResult(result);
        return result;
    }


    public <T> SingleResult<T> getSuccessResult() {
        SingleResult<T> result = new SingleResult<>();
        setSuccessResult(result);
        return result;
    }

    public <T> SingleResult<T> getFailResult(ErrorCode errorCode) {
        SingleResult<T> result = new SingleResult<>();

        result.setStatus(errorCode.getStatus());
        result.setCode(errorCode.getCode());
        result.setMessage(errorCode.getMessage());

        return result;
    }

    public <T> ListResult<T> getFailListResult(ErrorCode errorCode) {
        ListResult<T> result = new ListResult<>();
//        result.setSuccess(false);
        result.setStatus(errorCode.getStatus());
        result.setCode(errorCode.getCode());
        result.setMessage(errorCode.getMessage());
//        result.setMsg(msg);
        return result;
    }

    // 페이징 성공 처리
    public <T> PagingListResult<T> getPagingListResult(List<?> list ,Page<T> page) {
        PagingListResult<T> result = new PagingListResult<>();
        result.setData(list);
        result.setPageNumber(page.getNumber());
        result.setFirst(page.isFirst());
        result.setHasNext(page.hasNext());
        result.setTotalPageNumber(page.getTotalPages());
        setSuccessResult(result);
        return result;
    }

    // 페이징 실패 처리
    public <T> PagingListResult<T> getFailPagingListResult(ErrorCode errorCode) {
        PagingListResult<T> result = new PagingListResult<>();
        result.setStatus(errorCode.getStatus());
        result.setCode(errorCode.getCode());
        result.setMessage(errorCode.getMessage());
        return result;
    }
}