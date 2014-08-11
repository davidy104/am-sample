package com.aucklandmuseum.image.v1

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["imageName"])
class ImageTransform {
	String imageName
	Integer width
	Integer hight
}
