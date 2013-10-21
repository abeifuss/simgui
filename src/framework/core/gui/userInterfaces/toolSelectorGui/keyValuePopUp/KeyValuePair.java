package framework.core.gui.userInterfaces.toolSelectorGui.keyValuePopUp;

/**
 * A simple bean for a key and a value
 * @author Marius Fink
 * @version 01.06.2012
 */
public class KeyValuePair<A, B> {

    private A key;
    private B value;
    
    /**
     * Empty constructor
     */
    public KeyValuePair() {
    }
    
    /**
     * Constructor with initial values
     * @param key the initial key
     * @param value the initial value
     */
    public KeyValuePair(A key, B value) {
	setKey(key);
	setValue(value);
    }
    
    /**
     * @return the key
     */
    public A getKey() {
	return key;
    }
    /**
     * @param key the key to set
     */
    public void setKey(A key) {
	this.key = key;
    }
    /**
     * @return the value
     */
    public B getValue() {
	return value;
    }
    /**
     * @param value the value to set
     */
    public void setValue(B value) {
	this.value = value;
    }
    /**
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((key == null) ? 0 : key.hashCode());
	result = prime * result + ((value == null) ? 0 : value.hashCode());
	return result;
    }
    /**
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (!(obj instanceof KeyValuePair))
	    return false;
	@SuppressWarnings("rawtypes")
	KeyValuePair other = (KeyValuePair) obj;
	if (key == null) {
	    if (other.key != null)
		return false;
	} else if (!key.equals(other.key))
	    return false;
	if (value == null) {
	    if (other.value != null)
		return false;
	} else if (!value.equals(other.value))
	    return false;
	return true;
    }
    
    
}
