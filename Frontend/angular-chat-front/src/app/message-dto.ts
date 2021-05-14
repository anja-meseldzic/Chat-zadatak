export class MessageDto {
    constructor(
        public otherUsername : string,
        public incoming : boolean,
        public subject : string,
        public content : string,
        public dateTime : Date
    ) {}
}
