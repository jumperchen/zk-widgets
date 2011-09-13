package org.zkoss.imagelabel;

import java.io.IOException;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.sys.ContentRenderer;
import org.zkoss.zul.impl.LabelImageElement;

public class ImageLabel extends LabelImageElement {

	private static final long serialVersionUID = 20110728174542L;
	
	private static final String VERTICAL = "vertical", NORMAL = "normal";
	private String _label;
	private String _image;
	private String _orient = VERTICAL;
	private String _dir = NORMAL;
	
	public ImageLabel(){
	}
	
	public ImageLabel(String label) {
		_label = label;
	}
	
	public ImageLabel(String label, String image) {
		_label = label;
		_image = image;
	}
	
	/** Returns the orient.
	 * <p>Default: "horizontal".
	 */
	public String getOrient() {
		return _orient;
	}
	/** Sets the orient to layout image.
	 * @param orient either "horizontal" or "vertical".
	 */
	public void setOrient(String orient) throws WrongValueException {
		if (!VERTICAL.equals(orient) && !"horizontal".equals(orient))
			throw new WrongValueException(orient);

		if (!_orient.equals(orient)) {
			_orient = orient;
			smartUpdate("orient", _orient);
		}
	}
	
	/** Returns the direction.
	 * <p>Default: "normal".
	 */
	public String getDir() {
		return _dir;
	}
	/** Sets the direction to layout image.
	 * @param dir either "normal" or "reverse".
	 */
	public void setDir(String dir) throws WrongValueException {
		if (!NORMAL.equals(dir) && !"reverse".equals(dir))
			throw new WrongValueException(dir);

		if (!_dir.equals(dir)) {
			_dir = dir;
			smartUpdate("dir", _dir);
		}
	}
	
	//super//
	protected void renderProperties(ContentRenderer renderer) throws IOException {
		super.renderProperties(renderer);
		if (_label != null)
			render(renderer, "label", _label);
		if (_image != null)
			render(renderer, "image", _image);
		String s;
		if (!VERTICAL.equals(s = getOrient()))
			render(renderer, "orient", s);
		if (!NORMAL.equals(s = getDir()))
			render(renderer, "dir", s);
	}
	
	/**
	 * The default zclass is "z-imagelabel"
	 */
	public String getZclass() {
		return (this._zclass != null ? this._zclass : "z-imagelabel");
	}
}