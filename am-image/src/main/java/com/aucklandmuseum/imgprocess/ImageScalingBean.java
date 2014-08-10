package com.aucklandmuseum.imgprocess;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ImageScalingBean {

	private String imageName;

	private Integer width;

	private Integer hight;

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHight() {
		return hight;
	}

	public static Builder getBuilder(String imageName, Integer width,
			Integer hight) {
		return new Builder(imageName, width, hight);
	}

	public static Builder getBuilder(String imageName) {
		return new Builder(imageName);
	}

	public static class Builder {

		private ImageScalingBean built;

		public Builder(String imageName) {
			built = new ImageScalingBean();
			built.imageName = imageName;
		}

		public Builder(String imageName, Integer width, Integer hight) {
			built = new ImageScalingBean();
			built.imageName = imageName;
			built.width = width;
			built.hight = hight;
		}

		public ImageScalingBean build() {
			return built;
		}
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.imageName,
				((ImageScalingBean) obj).imageName).isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.imageName).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("imageName", imageName).append("width", width)
				.append("hight", hight).toString();
	}
}
