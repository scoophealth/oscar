package org.oscarehr.common.model;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.oscarehr.util.MiscUtils;

public abstract class AbstractModel<T> implements java.io.Serializable
{		
	protected static final String OBJECT_NOT_YET_PERISTED="The object is not persisted yet, this operation requires the object to already be persisted.";
	
	public abstract T getId();
	
	@Override
    public String toString()
	{
		return(ReflectionToStringBuilder.toString(this));
	}

	@Override
    public int hashCode()
	{
		if (getId() == null)
		{
			MiscUtils.getLogger().warn(OBJECT_NOT_YET_PERISTED, new Exception());
			return(super.hashCode());
		}
		
		return(getId().hashCode());
	}
	
	public boolean equals(AbstractModel<T> o)
	{
		if (getClass()!=o.getClass()) return(false);

		if (getId() == null)
		{
			MiscUtils.getLogger().warn(OBJECT_NOT_YET_PERISTED, new Exception());
		}

		return(getId().equals(o.getId()));
	}

	/**
	 * This method checks to see if there is an entry in the list with the corresponding primary key, it does not check to see that the other values are the
	 * same or not.
	 */
	public static <X extends AbstractModel<?>> boolean existsId(List<X> list, X searchModel)
	{
		Object searchPk = searchModel.getId();
		for (X tempModel : list)
		{
			Object tempPk = tempModel.getId();
			if (searchPk.equals(tempPk)) return(true);
		}

		return(false);
	}

}