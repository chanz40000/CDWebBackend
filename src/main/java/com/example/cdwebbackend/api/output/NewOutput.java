package com.example.cdwebbackend.api.output;

import com.example.cdwebbackend.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class NewOutput {
    private int page;
    private int totalPage;
    private List<UserDTO> listResult = new ArrayList<>();

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<UserDTO> getListResult() {
        return listResult;
    }

    public void setListResult(List<UserDTO> news) {
        this.listResult = news;
    }
}
