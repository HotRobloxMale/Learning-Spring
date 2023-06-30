package app.services;

import java.util.List;

public class GptAnswerResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<GptAnswerChoice> choices;
    private GptAnswerUsage usage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<GptAnswerChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<GptAnswerChoice> choices) {
        this.choices = choices;
    }

    public GptAnswerUsage getUsage() {
        return usage;
    }

    public void setUsage(GptAnswerUsage usage) {
        this.usage = usage;
    }
}
