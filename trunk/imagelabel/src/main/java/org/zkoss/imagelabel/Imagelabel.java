package org.zkoss.imagelabel;

import org.zkoss.zul.impl.XulElement;

public class Imagelabel extends XulElement {

	private static final long serialVersionUID = -4999861716845475461L;

	private String _label;
	private String _image;
	
	public Imagelabel(){
	}
	
	public Imagelabel(String label) {
		_label = label;
	}
	
	public Imagelabel(String label, String image) {
		_label = label;
		_image = image;
	}
	
	public String getLabel() {
		return _label;
	}

	public void setLabel(String label) {
		if(!label.equals(_label)) {
			_label = label;
			smartUpdate("label", _label);
		}
	}

	public String getImage() {
		return _image;
	}

	public void setImage(String image) {
		if(!image.equals(_image)) {
			_image = image;
			smartUpdate("image", _image);
		}
	}

	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer) throws java.io.IOException {
		super.renderProperties(renderer);
		if (_label != null)
			render(renderer, "label", _label);
		if (_image != null)
			render(renderer, "image", _image);
	}
	
	public String getZclass() {
		return _zclass == null ? "z-imagelabel" : _zclass;
	}
	
	public void setZclass(String _zclass) {
		this._zclass = _zclass;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
