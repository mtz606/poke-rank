package com.pokemon.rankings.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonCardResponse {
    private String id;
    private String name;
    
    @JsonProperty("images")
    private Map<String, String> images;
    
    @JsonProperty("set")
    private SetInfo set;
    
    private String rarity;
    
    @JsonProperty("number")
    private String number;
    
    public PokemonCardResponse() {}
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Map<String, String> getImages() {
        return images;
    }
    
    public void setImages(Map<String, String> images) {
        this.images = images;
    }
    
    public SetInfo getSet() {
        return set;
    }
    
    public void setSet(SetInfo set) {
        this.set = set;
    }
    
    public String getRarity() {
        return rarity;
    }
    
    public void setRarity(String rarity) {
        this.rarity = rarity;
    }
    
    public String getNumber() {
        return number;
    }
    
    public void setNumber(String number) {
        this.number = number;
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SetInfo {
        private String name;
        private String id;
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
    }
}

