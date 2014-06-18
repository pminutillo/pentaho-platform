package org.pentaho.platform.web.http.api.resources;

import org.pentaho.platform.repository2.unified.webservices.VersionSummaryDto;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.List;

/**
 * Created by pminutillo on 6/16/14.
 */

@XmlRootElement(name="responseList")
@XmlSeeAlso({VersionSummaryDto.class})
public class VersionsResponseList{
    List<Object> list;

    public List<Object> getList(){
        return list;
    }

    public void setList(List<Object> list){
        this.list = list;
    }
}
