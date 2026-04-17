package cn.langchain4j.ai.dto;



import lombok.Getter;

@Getter
public class ChatMessageDTO {

    private String role;   // user / ai / system
    private String content;

    public ChatMessageDTO() {}

    public ChatMessageDTO(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
