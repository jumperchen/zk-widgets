function(out) {
	var zclass = this.getZclass(), uuid = this.uuid;
	
	out.push('<div id="', uuid, '" class="', zclass, '>',
			 '<span id="', uuid, '-image"><img src="', this._image, '" /></span>',
			 '<span id="', uuid, '-label" class="', zclass, '-label">', this._label, '</span>',
			 '</div>');
};
