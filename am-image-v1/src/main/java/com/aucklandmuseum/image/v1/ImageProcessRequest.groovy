package com.aucklandmuseum.image.v1

import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
class ImageProcessRequest implements Serializable {
	String imagePath
	String imageFileName
	Set<ImageTransform> imageTransforms = new HashSet<ImageTransform>()
}
