/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opencastproject.ingest.endpoint;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author medialab
 */
@Entity
@Table(name = "media_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MediaInfo.findAll", query = "SELECT m FROM MediaInfo m"),
    @NamedQuery(name = "MediaInfo.findById", query = "SELECT m FROM MediaInfo m WHERE m.id = :id")})
public class MediaInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Lob
    @Column(name = "media_id")
    private String mediaId;
    @Basic(optional = false)
    @Lob
    @Column(name = "media_name")
    private String mediaName;
    @Basic(optional = false)
    @Lob
    @Column(name = "media_creator")
    private String mediaCreator;
    @Basic(optional = false)
    @Lob
    @Column(name = "duration")
    private String duration;
    @Basic(optional = false)
    @Lob
    @Column(name = "media_explanation")
    private String mediaExplanation;
    @Basic(optional = false)
    @Lob
    @Column(name = "uploader")
    private String uploader;

    public MediaInfo() {
    }

    public MediaInfo(Integer id) {
        this.id = id;
    }

    public MediaInfo(Integer id, String mediaId, String mediaName, String mediaCreator, String duration, String mediaExplanation, String uploader) {
        this.id = id;
        this.mediaId = mediaId;
        this.mediaName = mediaName;
        this.mediaCreator = mediaCreator;
        this.duration = duration;
        this.mediaExplanation = mediaExplanation;
        this.uploader = uploader;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getMediaCreator() {
        return mediaCreator;
    }

    public void setMediaCreator(String mediaCreator) {
        this.mediaCreator = mediaCreator;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMediaExplanation() {
        return mediaExplanation;
    }

    public void setMediaExplanation(String mediaExplanation) {
        this.mediaExplanation = mediaExplanation;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MediaInfo)) {
            return false;
        }
        MediaInfo other = (MediaInfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.opencastproject.ingest.endpoint.MediaInfo[ id=" + id + " ]";
    }
    
}
