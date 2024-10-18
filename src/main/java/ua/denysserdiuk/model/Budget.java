package ua.denysserdiuk.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "budget")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    private String description;
    private float amount;
    private String type;
    private LocalDate date;

    public Budget(){
    }
    public Budget(Users user, String description, float amount, String type, LocalDate date){
        this.user = user;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Budget{" +
                "id=" + id +
                ", user=" + user +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", date=" + date +
                '}';
    }
}

