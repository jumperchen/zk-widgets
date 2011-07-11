function(out) {
	var zclass = this.getZclass(), uuid = this.uuid;
	
	out.push('<div id="', uuid, '" class="', zclass, '">',
			 '<div id="', uuid, '-image" class="', zclass, '-labelvcenter"><img src="', this._image, '" />', this._label, '</div>',
			 '</div>');
};
