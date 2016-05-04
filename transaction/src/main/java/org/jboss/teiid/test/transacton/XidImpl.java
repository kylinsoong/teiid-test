package org.jboss.teiid.test.transacton;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigInteger;
import java.util.Arrays;

import javax.transaction.xa.Xid;


public class XidImpl implements Xid, Externalizable{
    
    private int formatID;
    private byte[] globalTransactionId;
    private byte[] branchQualifier;
    private String toString;
    
    public XidImpl() {
    }
    
    public XidImpl(Xid xid) {
        this.formatID = xid.getFormatId();
        this.globalTransactionId = xid.getGlobalTransactionId();
        this.branchQualifier = xid.getBranchQualifier();
    }
    
    public XidImpl(int formatID, byte[] globalTransactionId, byte[] branchQualifier){
        this.formatID = formatID;
        this.globalTransactionId = globalTransactionId;
        this.branchQualifier = branchQualifier;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(this.formatID);
        out.writeObject(this.globalTransactionId);
        out.writeObject(this.branchQualifier);
        
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException {
        this.formatID = in.readInt();
        this.globalTransactionId = (byte[])in.readObject();
        this.branchQualifier = (byte[])in.readObject();
        
    }

    @Override
    public int getFormatId() {
        return formatID;
    }

    @Override
    public byte[] getGlobalTransactionId() {
        return globalTransactionId;
    }

    @Override
    public byte[] getBranchQualifier() {
        return branchQualifier;
    }
    
    
    public boolean equals(Object obj){
        if(obj == this) {
            return true;
        } 
        if(!(obj instanceof XidImpl)){
            return false;
        }
        XidImpl that = (XidImpl)obj;
        return this.formatID == that.formatID
                && Arrays.equals(this.globalTransactionId, that.globalTransactionId)
                && Arrays.equals(this.branchQualifier, that.branchQualifier);
    }
    
    /** 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        if (toString == null) {
            StringBuffer sb = new StringBuffer();
            
            sb.append("Test-Xid global:"); //$NON-NLS-1$
            sb.append(getByteArrayString(globalTransactionId));
            sb.append(" branch:"); //$NON-NLS-1$
            sb.append(getByteArrayString(branchQualifier));
            sb.append(" format:"); //$NON-NLS-1$
            sb.append(getFormatId());
            toString = sb.toString();
        }
        return toString;
    }
    
    private String getByteArrayString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        
        return new BigInteger(bytes).toString();
    }
    
    /** 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return toString().hashCode();
    }

}
