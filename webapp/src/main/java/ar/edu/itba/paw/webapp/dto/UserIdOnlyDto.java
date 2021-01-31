package ar.edu.itba.paw.webapp.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class UserIdOnlyDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.user.v1";

    // Variables
    @NotNull(message = "UserIdOnlyDto.id.NotNull")
    private Integer id;

    // Constructor
    public UserIdOnlyDto() {
        // Use factory methods
    }

    // Getters&Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // Equals&HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserIdOnlyDto userIdDto = (UserIdOnlyDto) o;
        return Objects.equals(id, userIdDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
