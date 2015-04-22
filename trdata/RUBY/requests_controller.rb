class RequestsController < ApplicationController
  before_action :set_request, only: [:show, :edit, :update, :destroy, :flip, :add_attachment]

  # GET /requests
  # GET /requests.json
  def index
    @requests = Request.all
  end

  # GET /requests/1
  # GET /requests/1.json
  def show
  end

  def add_attachment

    accepted_formats = ['.doc', '.pdf', '.zip']

    data = params[:data]

    if accepted_formats.include? File.extname(data.original_filename)
      @request.attachments.create ({:filename => data.original_filename, :mime => data.content_type, :data => data.read})
      flash[:success] = "Dodano załącznik"
    else
      flash[:danger] = "Niedozwolony format załącznika"
    end
    redirect_to :action => :show
  end

  def flip
    if params[:accept]
      @request.status = "APPROVED"
      @request.comment = params[:comment]
    elsif params[:deny]
      @request.status = "REJECTED"
      @request.comment = params[:comment]
    end

    @request.save

    Notification.create ({student_id: @request.student_id, request_id: @request.id})

    flash[:success] = "Operacja zatwierdzona"
    redirect_to root_url
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_request
      @request = Request.find(params[:id])
      @attachments = @request.attachments
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def request_params
      params.require(:request).permit(:id_stud, :content, :timestamp)
    end
end
