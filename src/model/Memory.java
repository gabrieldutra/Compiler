/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gabriel
 */


public class Memory {
    private class MemoryEntry {
        private Identifier identifier;
        private Const value; // yay
        
        public MemoryEntry(Identifier identifier) {
            this.identifier = identifier;
            this.value = new Const(0, identifier.getType());
        }

        /**
         * @return the identifier
         */
        public Identifier getIdentifier() {
            return identifier;
        }

        /**
         * @param identifier the identifier to set
         */
        public void setIdentifier(Identifier identifier) {
            this.identifier = identifier;
        }

        /**
         * @return the value
         */
        public Const getValue() {
            return value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(Const value) {
            this.value = value;
        }
    }
    
    Map<String, MemoryEntry> memory;
    
    public Memory() {
        memory = new HashMap();
    }
    
    public boolean hasIdentifier(String id) {
        return memory.containsKey(id);
    }
    
    public void addIdentifier(Identifier identifier) {
        memory.put(identifier.getName(), new MemoryEntry(identifier));
    }
    
    public Type getType(String id) {
        return memory.get(id).getIdentifier().getType();
    }
    
    public Const getValue(String id) {
        return memory.get(id).getValue();
    }
    
    public void setValue(String id, Const value) {
        memory.get(id).setValue(value);
    }
    
}
