package org.zkoss.imagelabel;

import org.zkoss.zul.impl.LabelImageElement;

public class ImageLabel extends LabelImageElement {

	private static final long serialVersionUID = 20110728174542L;
	
	private String _label;
	private String _image;

	public ImageLabel(){
	}
	
	public ImageLabel(String label) {
		_label = label;
	}
	
	public ImageLabel(String label, String image) {
		_label = label;
		_image = image;
	}
	
	//super//
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer) throws java.io.IOException {
		super.renderProperties(renderer);
		if (_label != null)
			render(renderer, "label", _label);
		if (_image != null)
			render(renderer, "image", _image);
	}

	/**
	 * The default zclass is "z-imagelabel"
	 */
	public String getZclass() {
		return (this._zclass != null ? this._zclass : "z-imagelabel");
	}
}

