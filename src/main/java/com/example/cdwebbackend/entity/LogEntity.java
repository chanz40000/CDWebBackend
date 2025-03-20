package com.example.cdwebbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
public class LogEntity extends BaseEntity {

    // @Column: Lưu cấp độ log (ví dụ: INFO, WARN, ERROR)
    @Column(name = "level")
    private String level;

    // @Column: Lưu thời điểm tạo log
    @Column(name = "create_at")
    private LocalDateTime createAt;

    // @Column: Lưu địa chỉ nguồn (IP hoặc URL)
    @Column(name = "address")
    private String address;

    // @Column: Giá trị trước khi thay đổi (có thể null nếu không có)
    @Column(name = "pre_value", columnDefinition = "TEXT")
    private String preValue;

    // @Column: Giá trị sau khi thay đổi hoặc giá trị hiện tại
    @Column(name = "value", columnDefinition = "TEXT")
    private String value;

    // @Column: Quốc gia hoặc vùng lãnh thổ (dựa trên địa chỉ IP nếu có)
    @Column(name = "national")
    private String national;

    // @Column: Mô tả chi tiết về log (tuỳ chọn)
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // Getters và Setters

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPreValue() {
        return preValue;
    }

    public void setPreValue(String preValue) {
        this.preValue = preValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNational() {
        return national;
    }

    public void setNational(String national) {
        this.national = national;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
